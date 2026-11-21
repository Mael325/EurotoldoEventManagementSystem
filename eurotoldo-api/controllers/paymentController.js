function getPaymentsCollection(request) {
    return request.app.locals.database.collection("payments");
}

function getReservationsCollection(request) {
    return request.app.locals.database.collection("reservations");
}

function parsePositiveInteger(value) {
    const number = Number(value);

    if (!Number.isInteger(number) || number <= 0) {
        return null;
    }

    return number;
}

function parsePositiveAmount(value) {
    const number = Number(value);

    if (!Number.isFinite(number) || number <= 0) {
        return null;
    }

    return Math.round(number * 100) / 100;
}

function readRequiredText(value) {
    if (typeof value !== "string"
            || value.trim() === "") {

        return null;
    }

    return value.trim();
}

function createPaymentDocument(body) {
    const id = parsePositiveInteger(body.id);
    const reservationId = parsePositiveInteger(
        body.reservationId
    );
    const amount = parsePositiveAmount(body.amount);
    const paymentType = readRequiredText(
        body.paymentType
    );

    if (id === null) {
        return {
            error: "El código del pago es inválido."
        };
    }

    if (reservationId === null) {
        return {
            error: "El código de la reserva es inválido."
        };
    }

    if (amount === null) {
        return {
            error: "El valor del pago debe ser mayor que cero."
        };
    }

    if (paymentType !== "Cash"
            && paymentType !== "Transfer") {

        return {
            error: "El tipo de pago es inválido."
        };
    }

    let paymentDate = readRequiredText(
        body.paymentDate
    );

    if (paymentDate === null) {
        paymentDate = new Date()
            .toISOString()
            .replace("Z", "");
    }

    const payment = {
        id,
        reservationId,
        amount,
        paymentDate,
        paymentType
    };

    if (paymentType === "Cash") {
        const receivedBy = readRequiredText(
            body.receivedBy
        );

        if (receivedBy === null) {
            return {
                error: "Debe indicar quién recibió el efectivo."
            };
        }

        payment.receivedBy = receivedBy;
    }

    if (paymentType === "Transfer") {
        const bankName = readRequiredText(
            body.bankName
        );
        const transactionNumber = readRequiredText(
            body.transactionNumber
        );

        if (bankName === null) {
            return {
                error: "Debe indicar el nombre del banco."
            };
        }

        if (transactionNumber === null) {
            return {
                error: "Debe indicar el número de transacción."
            };
        }

        payment.bankName = bankName;
        payment.transactionNumber = transactionNumber;
    }

    return { payment };
}

async function calculatePaidAmount(
        paymentsCollection,
        reservationId) {

    const result = await paymentsCollection
        .aggregate([
            {
                $match: {
                    reservationId
                }
            },
            {
                $group: {
                    _id: null,
                    total: {
                        $sum: "$amount"
                    }
                }
            }
        ])
        .toArray();

    if (result.length === 0) {
        return 0;
    }

    return Number(result[0].total);
}

async function getPayments(request, response) {
    try {
        const payments = await getPaymentsCollection(
            request
        )
            .find(
                {},
                {
                    projection: {
                        _id: 0
                    }
                }
            )
            .sort({ id: 1 })
            .toArray();

        response.json(payments);

    } catch (error) {
        response.status(500).json({
            message: "No se pudieron consultar los pagos."
        });
    }
}

async function getPaymentById(request, response) {
    const id = parsePositiveInteger(
        request.params.id
    );

    if (id === null) {
        return response.status(400).json({
            message: "El código del pago es inválido."
        });
    }

    try {
        const payment = await getPaymentsCollection(
            request
        ).findOne(
            { id },
            {
                projection: {
                    _id: 0
                }
            }
        );

        if (payment === null) {
            return response.status(404).json({
                message: "El pago no fue encontrado."
            });
        }

        response.json(payment);

    } catch (error) {
        response.status(500).json({
            message: "No se pudo consultar el pago."
        });
    }
}

async function getPaymentsByReservation(
        request,
        response) {

    const reservationId = parsePositiveInteger(
        request.params.reservationId
    );

    if (reservationId === null) {
        return response.status(400).json({
            message: "El código de la reserva es inválido."
        });
    }

    try {
        const payments = await getPaymentsCollection(
            request
        )
            .find(
                { reservationId },
                {
                    projection: {
                        _id: 0
                    }
                }
            )
            .sort({ id: 1 })
            .toArray();

        response.json(payments);

    } catch (error) {
        response.status(500).json({
            message:
                "No se pudieron consultar los pagos de la reserva."
        });
    }
}

async function createPayment(request, response) {
    const paymentResult = createPaymentDocument(
        request.body
    );

    if (paymentResult.error) {
        return response.status(400).json({
            message: paymentResult.error
        });
    }

    const payment = paymentResult.payment;

    try {
        const paymentsCollection
                = getPaymentsCollection(request);

        const existingPayment
                = await paymentsCollection.findOne({
                    id: payment.id
                });

        if (existingPayment !== null) {
            return response.status(409).json({
                message:
                    "Ya existe un pago con ese código."
            });
        }

        const reservation
                = await getReservationsCollection(
                    request
                ).findOne({
                    id: payment.reservationId
                });

        if (reservation === null) {
            return response.status(404).json({
                message:
                    "La reserva indicada no existe."
            });
        }

        const status = String(
            reservation.status || ""
        ).toUpperCase();

        if (status === "CANCELLED"
                || status === "CANCELADA") {

            return response.status(400).json({
                message:
                    "No se puede pagar una reserva cancelada."
            });
        }

        const totalAmount = Number(
            reservation.totalAmount
        );

        if (!Number.isFinite(totalAmount)
                || totalAmount <= 0) {

            return response.status(400).json({
                message:
                    "El total de la reserva es inválido."
            });
        }

        const paidAmount
                = await calculatePaidAmount(
                    paymentsCollection,
                    payment.reservationId
                );

        const pendingBalance = Math.max(
            0,
            Math.round(
                (totalAmount - paidAmount) * 100
            ) / 100
        );

        if (payment.amount > pendingBalance) {
            return response.status(400).json({
                message:
                    "El pago supera el saldo pendiente.",
                pendingBalance
            });
        }

        await paymentsCollection.insertOne(payment);

        response.status(201).json({
            message: "Pago registrado correctamente.",
            payment
        });

    } catch (error) {
        response.status(500).json({
            message: "No se pudo registrar el pago."
        });
    }
}

async function deletePayment(request, response) {
    const id = parsePositiveInteger(
        request.params.id
    );

    if (id === null) {
        return response.status(400).json({
            message: "El código del pago es inválido."
        });
    }

    try {
        const result = await getPaymentsCollection(
            request
        ).findOneAndDelete({ id });

        if (result === null) {
            return response.status(404).json({
                message: "El pago no fue encontrado."
            });
        }

        delete result._id;

        response.json({
            message: "Pago eliminado correctamente.",
            payment: result
        });

    } catch (error) {
        response.status(500).json({
            message: "No se pudo eliminar el pago."
        });
    }
}

module.exports = {
    getPayments,
    getPaymentById,
    getPaymentsByReservation,
    createPayment,
    deletePayment
};
