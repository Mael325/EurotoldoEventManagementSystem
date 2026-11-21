const express = require("express");

const {
    getQuotes,
    getQuoteById,
    createQuote,
    updateQuote,
    approveQuote,
    deleteQuote
} = require("../controllers/quoteController");

const router = express.Router();

router.get("/", getQuotes);
router.get("/:id", getQuoteById);
router.post("/", createQuote);
router.put("/:id", updateQuote);
router.put("/:id/approve", approveQuote);
router.delete("/:id", deleteQuote);

module.exports = router;
