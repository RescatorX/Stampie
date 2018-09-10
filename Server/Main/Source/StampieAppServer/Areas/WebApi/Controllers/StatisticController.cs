using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using StampieAppServer.Areas.WebApi.Models;
using StampieAppServer.Data.Codebooks;
using StampieAppServer.Data.Entities;
using StampieAppServer.Models;

namespace StampieAppServer.Areas.WebApi.Controllers
{
    public class StatisticController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("statistic/getAllStatistics")]
        public IEnumerable<Statistic> GetAllStatistics()
        {
            return db.Statistics.Include(c => c.User);
        }

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("statistic/getUserStatistics")]
        public IEnumerable<Statistic> GetUserStatistics(Guid userId)
        {
            return db.Statistics.Include(c => c.User).Where(c => c.User.Id == userId);
        }

        [HttpPost]
        [Authorize]
        [System.Web.Mvc.Route("statistic/addStatistic")]
        public Statistic AddStatistic([FromBody] Statistic statistic)
        {
            statistic.User = db.AppUsers.FirstOrDefault(u => u.Id == statistic.User.Id);

            statistic = db.Statistics.Add(statistic);
            db.SaveChanges();

            return statistic;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("statistic/changeStatistic")]
        public Statistic ChangeStatistic([FromBody] Statistic statistic)
        {
            Statistic existingStatistic = db.Statistics.Find(statistic.Id);
            if (existingStatistic != null)
            {
                db.Statistics.AddOrUpdate(db.Statistics.Find(statistic.Id), statistic);
                db.SaveChanges();
            }
            return statistic;
        }
    }
}
