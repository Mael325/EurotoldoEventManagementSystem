const COLLECTION_NAME = "quotes";

function getQuotesCollection(request) {
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

function validateAssignments(assignments) {
    if (!Array.isArray(assignments)) {
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

function prepareQuote(body, id) {
    if (body === null
            || typeof body !== "object"
            || Array.isArray(body)) {
        return null;
    }

    const customer = body.customer;
    const event = body.event;
    const assignments = body.assignments;
    const issueDate = body.issueDate;

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
            || !isValidDate(issueDate)
            || !validateAssignments(assignments)) {

        return null;
    }

    return {
        id,
        customer,
        event,
        assignments,
        issueDate
    };
}

function calculateTotal(quote) {
    const start = new Date(
        quote.event.startDate + "T00:00:00Z"
    );

    const end = new Date(
        quote.event.endDate + "T00:00:00Z"
    );

    const rentalDays = Math.floor(
        (end.getTime() - start.getTime())
        / 86400000
    ) + 1;

    let total = 0;

    for (const assignment of quote.assignments) {
        total += Number(
            assignment.equipment.rentalPrice
        ) * Number(assignment.quantity)
                * rentalDays;
    }

    return Number(total.toFixed(2));
}

async function getQuotes(request, response) {
    try {
        const quotes = await getQuotesCollection(request)
                .find({}, { projection: { _id: 0 } })
                .sort({ id: 1 })
                .toArray();

        response.json(quotes);

    } catch (error) {
        console.error(
            "Error al consultar cotizaciones:",
            error.message
        );

        response.status(500).json({
            message:
                "No se pudieron consultar las cotizaciones."
        });
    }
}

async function getQuoteById(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message:
                    "El ID de la cotización no es válido."
            });
        }

        const quote = await getQuotesCollection(request)
                .findOne(
                    { id },
                    { projection: { _id: 0 } }
                );

        if (!quote) {
            return response.status(404).json({
                message: "Cotización no encontrada."
            });
        }

        response.json(quote);

    } catch (error) {
        console.error(
            "Error al buscar la cotización:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo buscar la cotización."
        });
    }
}

async function createQuote(request, response) {
    try {
        const id = parseId(request.body.id);
        const quoteData = prepareQuote(
            request.body, id
        );

        if (id === null || quoteData === null) {
            return response.status(400).json({
                message:
                    "Los datos de la cotización no son válidos."
            });
        }

        const collection = getQuotesCollection(request);
        const existingQuote
                = await collection.findOne({ id });

        if (existingQuote) {
            return response.status(409).json({
                message:
                    "Ya existe una cotización con ese ID."
            });
        }

        const quote = {
            ...quoteData,
            approved: false
        };

        await collection.insertOne(quote);

        response.status(201).json({
            message:
                "Cotización registrada correctamente.",
            quote
        });

    } catch (error) {
        console.error(
            "Error al registrar la cotización:",
            error.message
        );

        response.status(500).json({
            message:
                "No se pudo registrar la cotización."
        });
    }
}

async function updateQuote(request, response) {
    try {
        const id = parseId(request.params.id);
        const quoteData = prepareQuote(
            request.body, id
        );

        if (id === null || quoteData === null) {
            return response.status(400).json({
                message:
                    "Los datos de la cotización no son válidos."
            });
        }

        const collection = getQuotesCollection(request);
        const existingQuote
                = await collection.findOne({ id });

        if (!existingQuote) {
            return response.status(404).json({
                message: "Cotización no encontrada."
            });
        }

        if (existingQuote.approved) {
            return response.status(409).json({
                message:
                    "Una cotización aprobada no puede modificarse."
            });
        }

        await collection.updateOne(
            { id },
            {
                $set: {
                    ...quoteData,
                    approved: false
                }
            }
        );

        response.json({
            message:
                "Cotización actualizada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al actualizar la cotización:",
            error.message
        );

        response.status(500).json({
            message:
                "No se pudo actualizar la cotización."
        });
    }
}

async function approveQuote(request, response) {
    try {
        const id = parseId(request.params.id);
        const collection = getQuotesCollection(request);

        if (id === null) {
            return response.status(400).json({
                message:
                    "El ID de la cotización no es válido."
            });
        }

        const quote = await collection.findOne({ id });

        if (!quote) {
            return response.status(404).json({
                message: "Cotización no encontrada."
            });
        }

        if (quote.approved) {
            return response.status(409).json({
                message:
                    "La cotización ya está aprobada."
            });
        }

        if (!Array.isArray(quote.assignments)
                || quote.assignments.length === 0
                || calculateTotal(quote) <= 0) {

            return response.status(409).json({
                message:
                    "La cotización no tiene equipos válidos."
            });
        }

        await collection.updateOne(
            { id },
            { $set: { approved: true } }
        );

        response.json({
            message:
                "Cotización aprobada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al aprobar la cotización:",
            error.message
        );

        response.status(500).json({
            message:
                "No se pudo aprobar la cotización."
        });
    }
}

async function deleteQuote(request, response) {
    try {
        const id = parseId(request.params.id);
        const collection = getQuotesCollection(request);

        if (id === null) {
            return response.status(400).json({
                message:
                    "El ID de la cotización no es válido."
            });
        }

        const quote = await collection.findOne({ id });

        if (!quote) {
            return response.status(404).json({
                message: "Cotización no encontrada."
            });
        }

        if (quote.approved) {
            return response.status(409).json({
                message:
                    "No puede eliminarse una cotización aprobada."
            });
        }

        await collection.deleteOne({ id });

        response.json({
            message:
                "Cotización eliminada correctamente."
        });

    } catch (error) {
        console.error(
            "Error al eliminar la cotización:",
            error.message
        );

        response.status(500).json({
            message:
                "No se pudo eliminar la cotización."
        });
    }
}

module.exports = {
    getQuotes,
    getQuoteById,
    createQuote,
    updateQuote,
    approveQuote,
    deleteQuote
};
