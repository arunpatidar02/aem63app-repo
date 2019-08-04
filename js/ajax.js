// Server-side JavaScript for the ajax call
use(function () {
    var curRes = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+resource.getPath();
    var getVar = new Packages.org.apache.commons.httpclient.methods.GetMethod(curRes);
    var client = new Packages.org.apache.commons.httpclient.HttpClient();
    //var creds = new Packages.org.apache.commons.httpclient.UsernamePasswordCredentials("admin", "admin");
    //client.getParams().setAuthenticationPreemptive(true);
    //client.getState().setCredentials(org.apache.commons.httpclient.auth.AuthScope.ANY, creds);
    client.executeMethod(getVar);
    log.info("Inside call ajax");
});
