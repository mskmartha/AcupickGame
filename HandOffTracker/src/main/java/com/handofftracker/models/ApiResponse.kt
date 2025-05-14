package com.handofftracker.models


data class ApiResponse(
    val storeNumber: String,
    val eventUserId: String,
    val bestOTH5: String,
    val bestHandOffStartTime: String?,
    val totalPoints: String,
    val bestWaitTime: String,
    val numberOfOTH5EligibleOrder: Int,
    val numberOfTotalOrder: Int,
    val rules: List<Rule>
)

data class Rule(
    val ruleId: String,
    val ruleName: String,
    val expression: String,
    val score: Int,
    val priority: Int
)