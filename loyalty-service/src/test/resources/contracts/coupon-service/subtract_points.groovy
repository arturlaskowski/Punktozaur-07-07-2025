package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should subtract points from loyalty account"

    request {
        method 'POST'
        url $(consumer(regex('/loyalty-accounts/' + uuid() + '/subtract-points')),
                producer('/loyalty-accounts/00000000-0000-0000-0000-000000000000/subtract-points'))
        headers {
            contentType('application/json')
        }
        body([
                points: $(consumer(positiveInt()), producer(10))
        ])
    }

    response {
        status NO_CONTENT()
    }
}
