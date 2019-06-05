package com.bennyhuo.dsl.layout.v2

import kotlin.annotation.AnnotationTarget.*

@DslMarker
@Target(CLASS, TYPE, TYPEALIAS)
annotation class DslViewMarker

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

@DslViewMarker
interface DslViewParent