package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.isToday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class OfflineSessionRepo @Inject constructor(
    private val dao: SessionDao,
    private val planDao: PlanDao,
    private val exerciseDao: ExerciseDao,
) : SessionRepo {

    override val stream: Flow<List<Session>> =
        dao.stream().map {
            it.map { session ->
                session.toExternal(session.sets.toExternal())
            }
        }

    override suspend fun addSet(set: Set) {
        if (!dao.sessionExistsOn(localDate)) {
            createEmpty(localDate)
        }
        val currentSessionId = requireNotNull(dao.getSessionId(localDate)) {
            "Session does not exist"
        }
        dao.addSet(set.toEntity(currentSessionId, dao.getSetCount(currentSessionId)))
    }

    override suspend fun removeSet(set: Set) {
        if (!dao.sessionExistsOn(localDate)) {
            createEmpty(localDate)
        }
        val currentSessionId = requireNotNull(dao.getSessionId(localDate)) {
            "Session does not exist"
        }
        dao.removeSet(set.toEntity(currentSessionId, dao.getSetCount(currentSessionId)))
    }

    override suspend fun createEmpty(date: LocalDate) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentPlanId = requireNotNull(planDao.currentPlanId()) { "No plan active" }
        dao.upsert(SessionDataEntity(localDate, currentPlanId))
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao
            .session(date)
            .map { session ->
                if (session == null) return@map null
                session.toExternal(session.sets.toExternal())
            }
    }

    private suspend fun List<SetEntity>.toExternal(): List<Set> = mapNotNull {
        val exercise = exerciseDao.get(it.exerciseId) ?: return@mapNotNull null
        it.toExternal(exercise.toExternal())
    }

}