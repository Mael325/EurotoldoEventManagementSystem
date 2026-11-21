const express = require("express");

const {
    getReservations,
    getReservationById,
    createReservation,
    updateReservation,
    cancelReservation,
    completeReservation,
    deleteReservation
} = require("../controllers/reservationController");

const router = express.Router();

router.get("/", getReservations);
router.get("/:id", getReservationById);
router.post("/", createReservation);
router.put("/:id", updateReservation);

router.put(
    "/:id/cancel",
    cancelReservation
);

router.put(
    "/:id/complete",
    completeReservation
);

router.delete("/:id", deleteReservation);

module.exports = router;
