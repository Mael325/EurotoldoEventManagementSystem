const COLLECTION_NAME = "reservations";

const VALID_STATUSES = [
    "PENDING",
    "CONFIRMED",
    "CANCELLED",
    "COMPLETED"
];

function getReservationsCollection(request) {
    return request.app.locals.database.collection(COLLECTION_NAME);
}

function parseId(value) {
    const id = Number(value);

    if (!Number.isInteger(id) || id <= 0) {
        return null;
    }

    return id;
}

function isValidDate(value) {
    if (typeof value !== "string"
            || !/^\d{4}-\d{2}-\d{2}$/.test(value)) {
        return false;
    }

    const date = new Date(value + "T00:00:00Z");

    return !Number.isNaN(date.getTime())
            && date.toISOString().slice(0, 10) === value;
}

function calculateRentalDays(event) {
    const start = new Date(
        event.startDate + "T00:00:00Z"
    );

    const end = new Date(
        event.endDate + "T00:00:00Z"
    );

    const millisecondsPerDay = 86400000;

    return Math.floor(
        (end.getTime() - start.getTime())
        / millisecondsPerDay
    ) + 1;
}

function validateAssignments(assignments) {
    if (!Array.isArray(assignments)
            || assignments.length === 0) {
        return false;
    }

    for (const assignment of assignments) {
        if (assignment === null
                || typeof assignment !== "object"
                || !Number.isInteger(
                    Number(assignment.id))
                || Number(assignment.id) < 0
                || !Number.isInteger(
                    Number(assignment.quantity))
                || Number(assignment.quantity) <= 0
                || assignment.equipment === null
                || typeof assignment.equipment
                    !== "object"
                || parseId(assignment.equipment.id)
                    === null
                || !Number.isFinite(
                    Number(
                        assignment.equipment.rentalPrice
                    )
                )
                || Number(
                    assignment.equipment.rentalPrice
                ) < 0) {

            return false;
        }
    }

    return true;
}

function calculateTotal(event, assignments) {
    const rentalDays = calculateRentalDays(event);
    let total = 0;

    for (const assignment of assignments) {
        total += Number(
            assignment.equipment.rentalPrice
        ) * Number(assignment.quantity)
                * rentalDays;
    }

    return Number(total.toFixed(2));
}

function prepareReservation(body, id) {
    if (body === null
            || typeof body !== "object"
            || Array.isArray(body)) {
        return null;
    }

    const customer = body.customer;
    const event = body.event;
    const status = body.status;
    const reservationDate = body.reservationDate;
    const assignments = body.assignments;

    if (customer === null
            || typeof customer !== "object"
            || parseId(customer.id) === null
            || typeof customer.name !== "string"
            || !customer.name.trim()
            || event === null
            || typeof event !== "object"
            || parseId(event.id) === null
            || !isValidDate(event.startDate)
            || !isValidDate(event.endDate)
            || event.endDate < event.startDate
            || !VALID_STATUSES.includes(status)
            || !isValidDate(reservationDate)
            || !validateAssignments(assignments)) {

        return null;
    }

    const totalAmount = calculateTotal(
        event, assignments
    );

    if (totalAmount <= 0) {
        return null;
    }

    return {
        id,
        customer,
        event,
        status,
        reservationDate,
        assignments,
        totalAmount
    };
}

async function getReservations(request, response) {
    try {
        const reservations
                = await getReservationsCollection(request)
                        .find(
                            {},
                            { projection: { _id: 0 } }
                        )
                        .sort({ id: 1 })
                        .toArray();

        response.json(reservations);

    } catch (error) {
        console.error(
            "Error al consultar reservas:",
            error.message
        );

        response.status(500).json({
            message: "No se pudieron consultar las reservas."
        });
    }
}

async function getReservationById(
        request, response) {

    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID de la reserva no es válido."
            });
        }

        const reservation
                = await getReservationsCollection(request)
                        .findOne(
                            { id },
                            { projection: { _id: 0 } }
                        );

        if (!reservation) {
            return response.status(404).json({
                message: "Reserva no encontrada."
            });
        }

        response.json(reservation);

    } catch (error) {
        console.error(
            "Error al buscar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo buscar la reserva."
        });
    }
}

async function createReservation(request, response) {
    try {
        const id = parseId(request.body.id);

        const reservation = prepareReservation(
            request.body, id
        );

        if (id === null || reservation === null) {
            return response.status(400).json({
                message: "Los datos de la reserva no son válidos."
            });
        }

        const collection
                = getReservationsCollection(request);

        const existingReservation
                = await collection.findOne({ id });

        if (existingReservation) {
            return response.status(409).json({
                message: "Ya existe una reserva con ese ID."
            });
        }

        await collection.insertOne(reservation);

        response.status(201).json({
            message: "Reserva registrada correctamente.",
            reservation
        });

    } catch (error) {
        console.error(
            "Error al registrar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo registrar la reserva."
        });
    }
}

async function updateReservation(request, response) {
    try {
        const id = parseId(request.params.id);

        const reservation = prepareReservation(
            request.body, id
        );

        if (id === null || reservation === null) {
            return response.status(400).json({
                message: "Los datos de la reserva no son válidos."
            });
        }

        delete reservation.status;

        const result
                = await getReservationsCollection(request)
                        .updateOne(
                            { id },
                            { $set: reservation }
                        );

        if (result.matchedCount === 0) {
            return response.status(404).json({
                message: "Reserva no encontrada."
            });
        }

        response.json({
            message: "Reserva actualizada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al actualizar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo actualizar la reserva."
        });
    }
}

async function cancelReservation(request, response) {
    try {
        const id = parseId(request.params.id);
        const collection
                = getReservationsCollection(request);

        if (id === null) {
            return response.status(400).json({
                message: "El ID de la reserva no es válido."
            });
        }

        const reservation
                = await collection.findOne({ id });

        if (!reservation) {
            return response.status(404).json({
                message: "Reserva no encontrada."
            });
        }

        if (reservation.status === "CANCELLED"
                || reservation.status === "COMPLETED") {

            return response.status(409).json({
                message: "La reserva no puede cancelarse."
            });
        }

        await collection.updateOne(
            { id },
            { $set: { status: "CANCELLED" } }
        );

        response.json({
            message: "Reserva cancelada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al cancelar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo cancelar la reserva."
        });
    }
}

async function completeReservation(
        request, response) {

    try {
        const id = parseId(request.params.id);
        const collection
                = getReservationsCollection(request);

        if (id === null) {
            return response.status(400).json({
                message: "El ID de la reserva no es válido."
            });
        }

        const reservation
                = await collection.findOne({ id });

        if (!reservation) {
            return response.status(404).json({
                message: "Reserva no encontrada."
            });
        }

        if (reservation.status !== "CONFIRMED") {
            return response.status(409).json({
                message: "La reserva no puede completarse."
            });
        }

        await collection.updateOne(
            { id },
            { $set: { status: "COMPLETED" } }
        );

        response.json({
            message: "Reserva completada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al completar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo completar la reserva."
        });
    }
}

async function deleteReservation(
        request, response) {

    try {
        const id = parseId(request.params.id);
        const collection
                = getReservationsCollection(request);

        if (id === null) {
            return response.status(400).json({
                message: "El ID de la reserva no es válido."
            });
        }

        const reservation
                = await collection.findOne({ id });

        if (!reservation) {
            return response.status(404).json({
                message: "Reserva no encontrada."
            });
        }

        if (reservation.status !== "CANCELLED") {
            return response.status(409).json({
                message:
                    "Solo puede eliminarse una reserva cancelada."
            });
        }

        await collection.deleteOne({ id });

        response.json({
            message: "Reserva eliminada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al eliminar la reserva:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo eliminar la reserva."
        });
    }
}

module.exports = {
    getReservations,
    getReservationById,
    createReservation,
    updateReservation,
    cancelReservation,
    completeReservation,
    deleteReservation
};
