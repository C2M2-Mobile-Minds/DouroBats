package pt.dourobats.app.features.management.ui

internal class TemplateStore {
    private val _templates = mutableListOf<SessionTemplate>()
    val templates: List<SessionTemplate> get() = _templates.toList()

    fun add(template: SessionTemplate) {
        _templates.add(0, template) // newest first
    }
}
