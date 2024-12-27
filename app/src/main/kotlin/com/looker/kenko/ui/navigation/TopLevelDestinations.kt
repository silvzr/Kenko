package com.looker.kenko.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import com.looker.kenko.R
import com.looker.kenko.ui.home.navigation.HomeRoute
import com.looker.kenko.ui.performance.navigation.PerformanceRoute
import com.looker.kenko.ui.profile.navigation.ProfileRoute

// Manually add 80.dp padding for bottom app bar
enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: Any,
) {
    Performance(R.string.label_performance, Icons.Outlined.Timeline, PerformanceRoute),
    Home(R.string.label_home, Icons.Outlined.Home, HomeRoute),
    Profile(R.string.label_profile, Icons.Outlined.Person, ProfileRoute),
}
