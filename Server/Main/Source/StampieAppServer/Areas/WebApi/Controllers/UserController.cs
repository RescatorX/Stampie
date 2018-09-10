using System;
using System.Collections.Generic;
using System.Data.Entity.Migrations;
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

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("user/getUserById")]
        public User GetUserById(Guid id)
        {
            return db.AppUsers.FirstOrDefault(u => u.Id == id);
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
        [System.Web.Mvc.Route("user/changeUser")]
        public User ChangeUser([FromBody] User user)
        {
            User existingUser = db.AppUsers.Find(user.Id);
            if (existingUser != null)
            {
                db.AppUsers.AddOrUpdate(db.AppUsers.Find(user.Id), user);
                db.SaveChanges();
            }
            return user;
        }

        [HttpDelete]
        [Authorize]
        [System.Web.Mvc.Route("user/deleteUser")]
        public User DeleteUser(Guid id)
        {
            User user = db.AppUsers.FirstOrDefault(u => u.Id == id);
            if (user != null)
            {
                user = db.AppUsers.Remove(user);
                db.SaveChanges();
            }
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
