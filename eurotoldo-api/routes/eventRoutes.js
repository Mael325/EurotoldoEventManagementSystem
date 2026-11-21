const express = require("express");

const {
    getEvents,
    getEventById,
    createEvent,
    updateEvent,
    markSetupCompleted,
    markPickupCompleted,
    deleteEvent
} = require("../controllers/eventController");

const router = express.Router();

router.get("/", getEvents);
router.get("/:id", getEventById);
router.post("/", createEvent);
router.put("/:id", updateEvent);

router.put(
    "/:id/setup-completed",
    markSetupCompleted
);

router.put(
    "/:id/pickup-completed",
    markPickupCompleted
);

router.delete("/:id", deleteEvent);

module.exports = router;
