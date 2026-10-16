package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donations")
data class DeveloperDonation(
    @PrimaryKey
    val id: String,
    val appId: String,
    val appName: String,
    val developerName: String,
    val amountUsd: Double,
    val currency: String = "USD",
    val platform: String, // "Direct Supporter Tip", "Liberapay", "GitHub Sponsors", "Crypto (XMR/BTC)"
    val supporterMessage: String,
    val backerBadge: String, // "Bronze Backer", "Silver Sponsor", "Gold Patron", "Freedom Champion"
    val timestamp: Long
)
