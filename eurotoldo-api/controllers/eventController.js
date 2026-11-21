const COLLECTION_NAME = "events";

function getEventsCollection(request) {
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

function isValidDateTime(value) {
    if (typeof value !== "string"
            || !/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}/
                    .test(value)) {
        return false;
    }

    return !Number.isNaN(Date.parse(value));
}

function prepareEvent(body, id) {
    if (body === null
            || typeof body !== "object"
            || Array.isArray(body)) {
        return null;
    }

    const name = typeof body.name === "string"
        ? body.name.trim() : "";

    const eventType = typeof body.eventType === "string"
        ? body.eventType.trim() : "";

    const location = typeof body.location === "string"
        ? body.location.trim() : "";

    const startDate = body.startDate;
    const endDate = body.endDate;

    const setupDateTime = body.setupDateTime ?? null;
    const pickupDateTime = body.pickupDateTime ?? null;

    if (!name
            || !eventType
            || !location
            || !isValidDate(startDate)
            || !isValidDate(endDate)
            || endDate < startDate) {
        return null;
    }

    const bothSchedulesAreNull
            = setupDateTime === null
            && pickupDateTime === null;

    const bothSchedulesExist
            = isValidDateTime(setupDateTime)
            && isValidDateTime(pickupDateTime);

    if (!bothSchedulesAreNull
            && !bothSchedulesExist) {
        return null;
    }

    if (bothSchedulesExist) {
        if (setupDateTime > pickupDateTime
                || setupDateTime.slice(0, 10) > startDate
                || pickupDateTime.slice(0, 10) < endDate) {
            return null;
        }
    }

    return {
        id,
        name,
        eventType,
        location,
        startDate,
        endDate,
        setupDateTime,
        pickupDateTime
    };
}

async function getEvents(request, response) {
    try {
        const events = await getEventsCollection(request)
                .find({}, { projection: { _id: 0 } })
                .sort({ id: 1 })
                .toArray();

        response.json(events);

    } catch (error) {
        console.error(
            "Error al consultar eventos:",
            error.message
        );

        response.status(500).json({
            message: "No se pudieron consultar los eventos."
        });
    }
}

async function getEventById(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del evento no es válido."
            });
        }

        const event = await getEventsCollection(request)
                .findOne(
                    { id },
                    { projection: { _id: 0 } }
                );

        if (!event) {
            return response.status(404).json({
                message: "Evento no encontrado."
            });
        }

        response.json(event);

    } catch (error) {
        console.error(
            "Error al buscar el evento:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo buscar el evento."
        });
    }
}

async function createEvent(request, response) {
    try {
        const id = parseId(request.body.id);
        const eventData = prepareEvent(request.body, id);

        if (id === null || eventData === null) {
            return response.status(400).json({
                message: "Los datos del evento no son válidos."
            });
        }

        const collection = getEventsCollection(request);
        const existingEvent = await collection.findOne({ id });

        if (existingEvent) {
            return response.status(409).json({
                message: "Ya existe un evento con ese ID."
            });
        }

        const event = {
            ...eventData,
            setupCompleted: false,
            pickupCompleted: false
        };

        await collection.insertOne(event);

        response.status(201).json({
            message: "Evento registrado correctamente.",
            event
        });

    } catch (error) {
        console.error(
            "Error al registrar el evento:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo registrar el evento."
        });
    }
}

async function updateEvent(request, response) {
    try {
        const id = parseId(request.params.id);
        const eventData = prepareEvent(request.body, id);

        if (id === null || eventData === null) {
            return response.status(400).json({
                message: "Los datos del evento no son válidos."
            });
        }

        const result = await getEventsCollection(request)
                .updateOne(
                    { id },
                    { $set: eventData }
                );

        if (result.matchedCount === 0) {
            return response.status(404).json({
                message: "Evento no encontrado."
            });
        }

        response.json({
            message: "Evento actualizado correctamente.",
            event: eventData
        });

    } catch (error) {
        console.error(
            "Error al actualizar el evento:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo actualizar el evento."
        });
    }
}

async function markSetupCompleted(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del evento no es válido."
            });
        }

        const result = await getEventsCollection(request)
                .updateOne(
                    { id },
                    { $set: { setupCompleted: true } }
                );

        if (result.matchedCount === 0) {
            return response.status(404).json({
                message: "Evento no encontrado."
            });
        }

        response.json({
            message: "Montaje marcado como completado."
        });

    } catch (error) {
        console.error(
            "Error al completar el montaje:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo actualizar el montaje."
        });
    }
}

async function markPickupCompleted(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del evento no es válido."
            });
        }

        const collection = getEventsCollection(request);
        const event = await collection.findOne({ id });

        if (!event) {
            return response.status(404).json({
                message: "Evento no encontrado."
            });
        }

        if (!event.setupCompleted) {
            return response.status(409).json({
                message: "Primero debe completarse el montaje."
            });
        }

        await collection.updateOne(
            { id },
            { $set: { pickupCompleted: true } }
        );

        response.json({
            message: "Retiro marcado como completado."
        });

    } catch (error) {
        console.error(
            "Error al completar el retiro:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo actualizar el retiro."
        });
    }
}

async function deleteEvent(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del evento no es válido."
            });
        }

        const result = await getEventsCollection(request)
                .deleteOne({ id });

        if (result.deletedCount === 0) {
            return response.status(404).json({
                message: "Evento no encontrado."
            });
        }

        response.json({
            message: "Evento eliminado correctamente."
        });

    } catch (error) {
        console.error(
            "Error al eliminar el evento:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo eliminar el evento."
        });
    }
}

module.exports = {
    getEvents,
    getEventById,
    createEvent,
    updateEvent,
    markSetupCompleted,
    markPickupCompleted,
    deleteEvent
};
