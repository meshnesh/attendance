package com.mw.churchattendance.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.local.entity.parent.Parent

data class ChildWithParent(
    @Embedded val child: Child,
    @Relation(
        parentColumn = "parentFobId",
        entityColumn = "fobId"
    )
    val parent: Parent
)