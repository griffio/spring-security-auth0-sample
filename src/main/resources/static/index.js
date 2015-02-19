var Auth0Lock = require("auth0-lock");

(function (document, undefined) {

    var userProfile = undefined;

    function setupPage() {

        var domain = 'griffio-application.auth0.com';
        var cid = 'FZ7Acusjd1BEjf4nbdid6x9PTJLBrE8P';

        var widget = Auth0Lock(cid, domain);

        widget.show({
            focusInput: false,
            popup: true
        }, function (err, profile, token) {
            if (err) {
                console.error(err);
            }
            var container = document.createElement("span");
            container.setAttribute("id", "js-nickname");
            container.textContent = profile['nickname'];
            document.body.appendChild(container);
            userProfile = profile;
        });

    }

    setupPage();

})(window.document, void 0);
