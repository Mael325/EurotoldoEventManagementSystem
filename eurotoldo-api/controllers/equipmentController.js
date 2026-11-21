const COLLECTION_NAME = "equipment";

function getEquipmentCollection(request) {
    return request.app.locals.database.collection(COLLECTION_NAME);
}

function parseId(value) {
    const id = Number(value);

    if (!Number.isInteger(id) || id <= 0) {
        return null;
    }

    return id;
}

function prepareEquipment(body, id) {
    if (body === null
            || typeof body !== "object"
            || Array.isArray(body)) {
        return null;
    }

    const name = typeof body.name === "string"
        ? body.name.trim() : "";

    const rentalPrice = Number(body.rentalPrice);
    const totalQuantity = Number(body.totalQuantity);
    const availableQuantity = Number(body.availableQuantity);

    if (!name
            || !Number.isFinite(rentalPrice)
            || rentalPrice < 0
            || !Number.isInteger(totalQuantity)
            || totalQuantity < 0
            || !Number.isInteger(availableQuantity)
            || availableQuantity < 0
            || availableQuantity > totalQuantity) {
        return null;
    }

    const equipment = {
        ...body,
        id,
        name,
        rentalPrice,
        totalQuantity,
        availableQuantity
    };

    delete equipment._id;

    return equipment;
}

async function getEquipment(request, response) {
    try {
        const equipmentList = await getEquipmentCollection(request)
                .find({}, { projection: { _id: 0 } })
                .sort({ id: 1 })
                .toArray();

        response.json(equipmentList);

    } catch (error) {
        console.error(
            "Error al consultar equipos:",
            error.message
        );

        response.status(500).json({
            message: "No se pudieron consultar los equipos."
        });
    }
}

async function getEquipmentById(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del equipo no es válido."
            });
        }

        const equipment = await getEquipmentCollection(request)
                .findOne(
                    { id },
                    { projection: { _id: 0 } }
                );

        if (!equipment) {
            return response.status(404).json({
                message: "Equipo no encontrado."
            });
        }

        response.json(equipment);

    } catch (error) {
        console.error(
            "Error al buscar el equipo:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo buscar el equipo."
        });
    }
}

async function createEquipment(request, response) {
    try {
        const id = parseId(request.body.id);
        const equipment = prepareEquipment(
            request.body, id
        );

        if (id === null || equipment === null) {
            return response.status(400).json({
                message: "Los datos del equipo no son válidos."
            });
        }

        const collection = getEquipmentCollection(request);
        const existingEquipment = await collection.findOne({ id });

        if (existingEquipment) {
            return response.status(409).json({
                message: "Ya existe un equipo con ese ID."
            });
        }

        await collection.insertOne(equipment);

        response.status(201).json({
            message: "Equipo registrado correctamente.",
            equipment
        });

    } catch (error) {
        console.error(
            "Error al registrar el equipo:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo registrar el equipo."
        });
    }
}

async function updateEquipment(request, response) {
    try {
        const id = parseId(request.params.id);
        const equipment = prepareEquipment(
            request.body, id
        );

        if (id === null || equipment === null) {
            return response.status(400).json({
                message: "Los datos del equipo no son válidos."
            });
        }

        const result = await getEquipmentCollection(request)
                .updateOne(
                    { id },
                    { $set: equipment }
                );

        if (result.matchedCount === 0) {
            return response.status(404).json({
                message: "Equipo no encontrado."
            });
        }

        response.json({
            message: "Equipo actualizado correctamente.",
            equipment
        });

    } catch (error) {
        console.error(
            "Error al actualizar el equipo:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo actualizar el equipo."
        });
    }
}

async function deleteEquipment(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del equipo no es válido."
            });
        }

        const result = await getEquipmentCollection(request)
                .deleteOne({ id });

        if (result.deletedCount === 0) {
            return response.status(404).json({
                message: "Equipo no encontrado."
            });
        }

        response.json({
            message: "Equipo eliminado correctamente."
        });

    } catch (error) {
        console.error(
            "Error al eliminar el equipo:",
            error.message
        );

        response.status(500).json({
            message: "No se pudo eliminar el equipo."
        });
    }
}

module.exports = {
    getEquipment,
    getEquipmentById,
    createEquipment,
    updateEquipment,
    deleteEquipment
};
