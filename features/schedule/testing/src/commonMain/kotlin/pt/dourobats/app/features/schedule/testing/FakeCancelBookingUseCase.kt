package pt.dourobats.app.features.schedule.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.usecase.CancelBookingUseCase

fun fakeCancelBookingUseCase(builder: FakeCancelBookingUseCase.() -> Unit = {}): CancelBookingUseCase =
    FakeCancelBookingUseCase().apply(builder).build()

class FakeCancelBookingUseCase {
    var invoke: suspend (sessionId: String) -> Result<Unit> =
        { _ -> throw NotImplementedError() }

    fun build(): CancelBookingUseCase =
        object : CancelBookingUseCase {
            override suspend fun invoke(sessionId: String): Result<Unit> =
                this@FakeCancelBookingUseCase.invoke(sessionId)
        }
}
