package com.example.news.classes

import java.util.*


import kotlinx.serialization.Serializable

@Serializable
data class TalkMassage(var category: Int = 0,
                        var content: String? = null,
                        var date: String? = null,
                        var messageId: Int = 0,
                        var number: Int = 0,
                        var spotId: Int = 0,
                        var userId: Int = 0
) {
    companion object{
        const val CATEGORY_MIDDLE_MASSAGE=0
        const val CATEGORY_MASSAGE=1
        const val CATEGORY_IMAGE=2


        const val MIDDLE_MASSAGE=0
        const val MYSELF_MASSAGE=1
        const val MYSELF_IMAGE=2
        const val OTHER_MASSAGE=3
        const val OTHER_IMAGE=4
    }

}