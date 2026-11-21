require("dotenv").config();

const express = require("express");
const { MongoClient } = require("mongodb");

const customerRoutes = require("./routes/customerRoutes");
const equipmentRoutes = require("./routes/equipmentRoutes");
const eventRoutes = require("./routes/eventRoutes");
const reservationRoutes = require(
    "./routes/reservationRoutes"
);

const quoteRoutes = require("./routes/quoteRoutes");

const paymentRoutes = require("./routes/paymentRoutes");

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());

if (!process.env.MONGO_URI) {
    console.error("No se encontró la dirección de MongoDB.");
    process.exit(1);
}

const client = new MongoClient(process.env.MONGO_URI);

app.get("/", (request, response) => {
    response.json({
        message: "La API de Eurotoldo está funcionando"
    });
});

app.use("/api/customers", customerRoutes);
app.use("/api/equipment", equipmentRoutes);
app.use("/api/events", eventRoutes);
app.use("/api/reservations", reservationRoutes);
app.use("/api/quotes", quoteRoutes);
app.use("/api/payments", paymentRoutes);

async function startServer() {
    try {
        await client.connect();

        const database = client.db(process.env.DB_NAME);
        await database.command({ ping: 1 });

        app.locals.database = database;

        console.log(
            `Conectado a MongoDB: ${process.env.DB_NAME}`
        );

        app.listen(port, () => {
            console.log(
                `API ejecutándose en http://localhost:${port}`
            );
        });

    } catch (error) {
        console.error(
            "Error al conectar con MongoDB:",
            error.message
        );

        process.exit(1);
    }
}

startServer();
