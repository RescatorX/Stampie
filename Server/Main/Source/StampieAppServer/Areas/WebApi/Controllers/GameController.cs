using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using StampieAppServer.Data.Entities;
using StampieAppServer.Models;

namespace StampieAppServer.Areas.WebApi.Controllers
{
    public class GameController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("game/getAllGames")]
        public IEnumerable<Game> GetAllGames()
        {
            return db.Games;
        }

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("game/getAllUserGames")]
        public IEnumerable<UserGame> GetAllUserGames()
        {
            return db.UserGames.Include(ug => ug.User).Include(ug => ug.Game);
        }

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("game/getUserGameById")]
        public UserGame GetUserGameById(Guid id)
        {
            return db.UserGames.Include(ug => ug.User).Include(ug => ug.Game).FirstOrDefault(ug => ug.Id == id);
        }

        [HttpPost]
        [Authorize]
        [System.Web.Mvc.Route("game/addUserGame")]
        public UserGame AddUserGame([FromBody] UserGame userGame)
        {
            userGame.User = db.AppUsers.FirstOrDefault(u => u.Id == userGame.User.Id);
            userGame.Game = db.Games.FirstOrDefault(g => g.Id == userGame.Game.Id);

            userGame = db.UserGames.Add(userGame);
            db.SaveChanges();

            return userGame;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("game/changeUserGame")]
        public UserGame ChangeUserGame([FromBody] UserGame userGame)
        {
            UserGame existingUserGame = db.UserGames.Find(userGame.Id);
            if (existingUserGame != null)
            {
                db.UserGames.AddOrUpdate(db.UserGames.Find(userGame.Id), userGame);
                db.SaveChanges();
            }
            return userGame;
        }
    }
}
