const COLLECTION_NAME = "customers";

function getCustomersCollection(request) {
    return request.app.locals.database.collection(COLLECTION_NAME);
}

function parseId(value) {
    const id = Number(value);

    if (!Number.isInteger(id) || id <= 0) {
        return null;
    }

    return id;
}

function readCustomerFields(body) {
    const name = typeof body.name === "string"
        ? body.name.trim() : "";

    const phone = typeof body.phone === "string"
        ? body.phone.trim() : "";

    const email = typeof body.email === "string"
        ? body.email.trim() : "";

    const city = typeof body.city === "string"
        ? body.city.trim() : "";

    if (!name || !phone || !email || !city) {
        return null;
    }

    return {
        name,
        phone,
        email,
        city
    };
}

async function getCustomers(request, response) {
    try {
        const customers = await getCustomersCollection(request)
                .find({}, { projection: { _id: 0 } })
                .sort({ id: 1 })
                .toArray();

        response.json(customers);
    } catch (error) {
        console.error("Error al consultar clientes:", error.message);

        response.status(500).json({
            message: "No se pudieron consultar los clientes."
        });
    }
}

async function getCustomerById(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del cliente no es válido."
            });
        }

        const customer = await getCustomersCollection(request).findOne(
            { id },
            { projection: { _id: 0 } }
        );

        if (!customer) {
            return response.status(404).json({
                message: "Cliente no encontrado."
            });
        }

        response.json(customer);
    } catch (error) {
        console.error("Error al buscar el cliente:", error.message);

        response.status(500).json({
            message: "No se pudo buscar el cliente."
        });
    }
}

async function createCustomer(request, response) {
    try {
        const id = parseId(request.body.id);
        const fields = readCustomerFields(request.body);

        if (id === null || fields === null) {
            return response.status(400).json({
                message: "Los datos del cliente están incompletos."
            });
        }

        const collection = getCustomersCollection(request);
        const existingCustomer = await collection.findOne({ id });

        if (existingCustomer) {
            return response.status(409).json({
                message: "Ya existe un cliente con ese ID."
            });
        }

        const customer = {
            id,
            ...fields
        };

        await collection.insertOne(customer);

        response.status(201).json({
            message: "Cliente registrado correctamente.",
            customer
        });
    } catch (error) {
        console.error("Error al registrar el cliente:", error.message);

        response.status(500).json({
            message: "No se pudo registrar el cliente."
        });
    }
}

async function updateCustomer(request, response) {
    try {
        const id = parseId(request.params.id);
        const fields = readCustomerFields(request.body);

        if (id === null || fields === null) {
            return response.status(400).json({
                message: "Los datos del cliente no son válidos."
            });
        }

        const result = await getCustomersCollection(request).updateOne(
            { id },
            { $set: fields }
        );

        if (result.matchedCount === 0) {
            return response.status(404).json({
                message: "Cliente no encontrado."
            });
        }

        response.json({
            message: "Cliente actualizado correctamente.",
            customer: {
                id,
                ...fields
            }
        });
    } catch (error) {
        console.error("Error al actualizar el cliente:", error.message);

        response.status(500).json({
            message: "No se pudo actualizar el cliente."
        });
    }
}

async function deleteCustomer(request, response) {
    try {
        const id = parseId(request.params.id);

        if (id === null) {
            return response.status(400).json({
                message: "El ID del cliente no es válido."
            });
        }

        const result = await getCustomersCollection(request)
                .deleteOne({ id });

        if (result.deletedCount === 0) {
            return response.status(404).json({
                message: "Cliente no encontrado."
            });
        }

        response.json({
            message: "Cliente eliminado correctamente."
        });
    } catch (error) {
        console.error("Error al eliminar el cliente:", error.message);

        response.status(500).json({
            message: "No se pudo eliminar el cliente."
        });
    }
}

module.exports = {
    getCustomers,
    getCustomerById,
    createCustomer,
    updateCustomer,
    deleteCustomer
};
