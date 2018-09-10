using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Principal;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using StampieAppServer.Models;

namespace StampieAppServer.Installers
{
    public class AuthorizationHeaderHandler : DelegatingHandler
    {
        #region Send method.

        /// <summary>   
        /// Send method.   
        /// </summary>   
        /// <param name="request">Request parameter</param>   
        /// <param name="cancellationToken">Cancellation token parameter</param>   
        /// <returns>Return HTTP response.</returns>   
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            // Initialization.   
            IEnumerable<string> apiKeyHeaderValues = null;
            AuthenticationHeaderValue authorization = request.Headers.Authorization;
            string userName = null;
            string password = null;
            // Verification.   
            if (request.Headers.TryGetValues(ApiInfo.API_KEY_HEADER, out apiKeyHeaderValues) && !string.IsNullOrEmpty(authorization.Parameter))
            {
                var apiKeyHeaderValue = apiKeyHeaderValues.First();
                // Get the auth token   
                string authToken = authorization.Parameter;
                // Decode the token from BASE64   
                string decodedToken = Encoding.UTF8.GetString(Convert.FromBase64String(authToken));
                // Extract username and password from decoded token   
                userName = decodedToken.Substring(0, decodedToken.IndexOf(":"));
                password = decodedToken.Substring(decodedToken.IndexOf(":") + 1);

                //ApplicationSignInManager signInManager = request.GetOwinContext().Get<ApplicationSignInManager>();
                ApplicationUserManager userManager = request.GetOwinContext().GetUserManager<ApplicationUserManager>();
                ApplicationSignInManager signInManager = request.GetOwinContext().GetUserManager<ApplicationSignInManager>();

                ApplicationUser user = userManager.FindByName(userName);

                //check for credentials before sign in ..    
                var validCredentials = signInManager.UserManager.CheckPassword(user, password);
                if (validCredentials)
                {
                    // Setting principal  
                    var identity = new GenericIdentity(userName);
                    SetPrincipal(new GenericPrincipal(identity, null));
                }
            }
            // Info.   
            return base.SendAsync(request, cancellationToken);
        }
        
        #endregion

        #region Set principal method.  

        /// <summary>   
        /// Set principal method.   
        /// </summary>   
        /// <param name="principal">Principal parameter</param>   
        private static void SetPrincipal(IPrincipal principal)
        {
            // setting.   
            Thread.CurrentPrincipal = principal;
            // Verification.   
            if (HttpContext.Current != null)
            {
                // Setting.   
                HttpContext.Current.User = principal;
            }
        }
        
        #endregion
    }
}