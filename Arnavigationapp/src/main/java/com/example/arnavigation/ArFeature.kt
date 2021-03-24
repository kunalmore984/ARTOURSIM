package com.example.arnavigation

sealed class ArFeature(
    val drawableId: Int,
    val isLaneVisible: Boolean,
    val isFenceVisible: Boolean
)  {
    abstract fun getNextFeature(): ArFeature

    object Lane : ArFeature(
        drawableId = R.drawable.ic_bad_route,
        isLaneVisible = true,
        isFenceVisible = false
    ) {
        override fun getNextFeature() = Fence
    }

    object Fence : ArFeature(
        drawableId = R.drawable.ic_baseline_arrow_back_ios_new_24,
        isLaneVisible = false,
        isFenceVisible = true
    ) {
        override fun getNextFeature() = LaneAndFence
    }

    object LaneAndFence : ArFeature(
        drawableId = R.drawable.ic_baseline_label_24,
        isLaneVisible = true,
        isFenceVisible = true
    ) {
        override fun getNextFeature() = Lane
    }
}