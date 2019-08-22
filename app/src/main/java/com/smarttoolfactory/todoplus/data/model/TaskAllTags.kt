package com.smarttoolfactory.todoplus.data.model

import androidx.room.Embedded
import androidx.room.Relation


class TaskAllTags () {

    @Embedded
    var task: Task? = null

    @Relation(parentColumn = "task_id", entityColumn = "tag_id", entity = Tag::class)
    var tags: List<Tag>? = null
}