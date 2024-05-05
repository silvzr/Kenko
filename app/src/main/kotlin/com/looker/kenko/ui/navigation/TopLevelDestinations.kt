package com.looker.kenko.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.kenko.R
import com.looker.kenko.ui.profile.navigation.PROFILE_ROUTE
import com.looker.kenko.ui.sessions.navigation.SESSIONS_ROUTE
import com.looker.kenko.ui.theme.KenkoIcons

enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: String,
) {
    Performance(R.string.label_performance, KenkoIcons.Performance, "performance"),
    Home(R.string.label_home, KenkoIcons.Home, SESSIONS_ROUTE),
    Profile(R.string.label_profile, KenkoIcons.Remove, PROFILE_ROUTE),
}
