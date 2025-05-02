
package com.example.acupickapp.Model

data class Example(
    val storeNumber: String,
    val eventuserid: String,
    val userData: UserData,
)

data class UserData(
    val totalpoints: String,
    val bestWaitTime: String,
    val bestOth5: String,
    val bestHandOffStartTime: String,
    val rules: List<Rule>,
    val eligibleOrderNumber: String,
    val totalOrderNumber: String,
)

data class Rule(
    val ruleId: String,
    val ruleName: String,
    val expression: String,
    val score: List<Score>,
    val priority: Int,
)

data class Score (
    val minTime: Int,
    val maxTime: Int,
    val score: Int,
)