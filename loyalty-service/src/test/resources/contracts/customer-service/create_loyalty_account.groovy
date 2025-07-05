package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should create loyalty account"

    request {
        method 'POST'
        url '/loyalty-accounts'
        headers {
            contentType('application/json')
        }
        body([
                customerId: $(consumer(anyUuid()), producer('12345678-1234-1234-1234-123456789012'))
        ])
        bodyMatchers {
            jsonPath('$.customerId', byRegex(uuid()))
        }
    }

    response {
        status CREATED()
        headers {
            header('Location', $(anyUrl()))
        }
    }
}