using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace StampieAppServer.Areas.Web.Controllers
{
    public class DefaultController : Controller
    {
        // GET: Web/Default
        public ActionResult Index()
        {
            return View();
        }
    }
}