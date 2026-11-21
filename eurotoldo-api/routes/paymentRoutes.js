const express = require("express");

const {
    getPayments,
    getPaymentById,
    getPaymentsByReservation,
    createPayment,
    deletePayment
} = require("../controllers/paymentController");

const router = express.Router();

router.get("/", getPayments);

router.get(
    "/reservation/:reservationId",
    getPaymentsByReservation
);

router.get("/:id", getPaymentById);
router.post("/", createPayment);
router.delete("/:id", deletePayment);

module.exports = router;
