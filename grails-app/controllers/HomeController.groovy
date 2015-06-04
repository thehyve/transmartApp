import org.transmart.searchapp.AuthUser
import org.transmartproject.core.users.User

class HomeController {
    User currentUserBean

    def index() {
        def model = [
                user: currentUserBean.username,
                isAdmin: AuthUser.get(currentUserBean.id).isAdmin(),
        ]

        render view: 'index', model: model
    }
}
