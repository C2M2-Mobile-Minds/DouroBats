package pt.dourobats.app.features.login.api.usecase

import pt.dourobats.app.core.common.Result

interface VerifyLoginCodeUseCase {
    suspend operator fun invoke(email: String, code: String): Result<Unit>
}
