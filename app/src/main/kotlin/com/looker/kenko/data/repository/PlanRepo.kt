package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PlanRepo {

    val stream: Flow<List<Plan>>

    val current: Flow<Plan?>

    fun exercises(date: LocalDate): Flow<List<Exercise>?>

    suspend fun get(id: Int): Plan?

    suspend fun switchPlan(plan: Plan)

    suspend fun upsert(plan: Plan)

    suspend fun remove(id: Int)

    suspend fun current(): Plan?

}