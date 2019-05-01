package org.rentalhouse.assets.contract

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.loader.PactFolder
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ["server.port=8080"])
@ActiveProfiles("contract-test")
@Provider("assetService")
@PactFolder("src/test/resources/pacts")
class AssetExistenceServiceProviderContractTest {

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @BeforeEach
    fun setUp() {
        assetRepository.deleteAll()
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("asset with id 1000 exists")
    fun returnsAsset() {
        val asset = asset(id = "1000") {
            plotIdentifier = "b/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
                state = "MH"
            }
        }

        assetRepository.save(asset)
    }

    @State("asset with id 1002 does not exist")
    fun missingAsset() {
    }
}