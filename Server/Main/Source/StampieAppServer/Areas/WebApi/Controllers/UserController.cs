using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using StampieAppServer.Data.Entities;
using StampieAppServer.Models;

namespace StampieAppServer.Areas.WebApi.Controllers
{
    public class UserController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("user/getAllUsers")]
        public IEnumerable<User> GetAllUsers()
        {
            return db.AppUsers;
        }

        [HttpPost]
        [Authorize]
        [System.Web.Mvc.Route("user/addUser")]
        public User AddUser([FromBody] User user)
        {
            user = db.AppUsers.Add(user);
            db.SaveChanges();

            return user;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("user/loginUser")]
        public User LoginUser([FromBody] User user)
        {
            user.LastLogin = DateTime.Now;
            db.SaveChanges();

            return user;
        }
    }
}
