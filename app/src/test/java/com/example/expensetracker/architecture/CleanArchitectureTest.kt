package com.example.expensetracker.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.classes
import com.lemonappdev.konsist.api.ext.list.interfaces
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class CleanArchitectureTest {

    @Test
    fun `use cases should reside in domain usecase package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("com.example.expensetracker.domain.usecase..") }
    }

    @Test
    fun `repositories in domain layer should be interfaces`() {
        Konsist
            .scopeFromPackage("com.example.expensetracker.domain.repository..")
            .interfaces()
            .assertTrue { it.name.endsWith("Repository") }
    }

    @Test
    fun `domain layer should not depend on data or feature layers`() {
        Konsist
            .scopeFromPackage("com.example.expensetracker.domain..")
            .files
            .assertTrue { file ->
                file.imports.none { 
                    it.name.contains("com.example.expensetracker.data") ||
                    it.name.contains("com.example.expensetracker.feature") ||
                    it.name.contains("com.example.expensetracker.presentation")
                }
            }
    }

    @Test
    fun `data layer should not depend on feature layers`() {
        Konsist
            .scopeFromPackage("com.example.expensetracker.data..")
            .files
            .assertTrue { file ->
                file.imports.none { 
                    it.name.contains("com.example.expensetracker.feature") ||
                    it.name.contains("com.example.expensetracker.presentation")
                }
            }
    }
}
