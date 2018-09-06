using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(StampieAppServer.Startup))]
namespace StampieAppServer
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
