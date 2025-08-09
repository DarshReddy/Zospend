package com.assignment.zospend.data.mock

import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.domain.model.Category
import java.time.Instant
import kotlin.random.Random

object MockExpense {
    private val vendors = listOf(
        "Amazon", "Flipkart", "Metro Cash & Carry", "Office Depot",
        "Swiggy", "Zomato", "IRCTC", "Uber", "Rapido", "BlueDart",
        "Vistara", "IndiGo", "DHL", "DMart", "Croma", "Apple Reseller"
    )
    private val cities = listOf("Bengaluru", "Mumbai", "Pune", "Hyderabad", "Chennai", "Delhi")
    private val categories =
        listOf("Travel", "Meals", "Supplies", "Software", "Logistics", "Events")
    private val payments = listOf("UPI", "Credit Card", "Debit Card", "NetBanking", "Cash")

    private fun randomTitle(r: Random): String {
        val vendor = vendors.random(r)
        return when (r.nextInt(5)) {
            0 -> "Office Supplies — $vendor"
            1 -> "Team Lunch — $vendor"
            2 -> "Local Travel — $vendor"
            3 -> "Subscription — $vendor"
            else -> "Courier Charges — $vendor"
        }
    }

    private fun randomNote(r: Random): String {
        val city = cities.random(r)
        val cat = categories.random(r)
        val pay = payments.random(r)
        val ref = r.nextInt(100_000, 999_999)
        return "$cat in $city • Paid via $pay • Ref #$ref"
    }

    private fun randomAmountMinor(r: Random): Long {
        // ₹100.00 to ₹25,000.00; paise at 0 or 50 for realism
        val rupees = r.nextInt(100, 25_000)
        val paise = if (r.nextBoolean()) 0 else 50
        return rupees * 100L + paise
    }

    private fun receiptUrl(title: String, r: Random): String {
        val w = listOf(480, 600, 720).random(r)
        val h = listOf(720, 800, 960).random(r)
        return "https://placehold.co/${w}x${h}?text=$title"
    }

    fun next(random: Random = Random.Default, createdAt: Instant): Expense {
        val title = randomTitle(random)
        return Expense(
            title = title,
            note = randomNote(random),
            amount = randomAmountMinor(random),
            category = Category.entries.random(),
            receiptUri = receiptUrl(title, random),
            createdAt = createdAt
        )
    }
}