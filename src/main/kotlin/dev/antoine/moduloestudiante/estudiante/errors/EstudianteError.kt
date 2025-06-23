package dev.antoine.moduloestudiante.estudiante.errors

sealed class EstudianteError(val message: String) {
    class LoadJson(message: String) : EstudianteError(message)
    class SaveJson(message: String) : EstudianteError(message)
    class LoadImage(message: String) : EstudianteError(message)
    class SaveImage(message: String) : EstudianteError(message)
    class DeleteImage(message: String) : EstudianteError(message)
    class deleteById(message: String) : EstudianteError(message)
    class ValidationProblem(message: String) : EstudianteError(message)
    class NotFound(message: String) : EstudianteError(message)
    class ExportZip(message: String) : EstudianteError(message)
    class ImportZip(message: String) : EstudianteError(message)

}