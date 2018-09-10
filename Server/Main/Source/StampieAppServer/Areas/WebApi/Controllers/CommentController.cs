using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Principal;
using System.Web;
using System.Web.Http;

using StampieAppServer.Areas.WebApi.Models;
using StampieAppServer.Data.Codebooks;
using StampieAppServer.Data.Entities;
using StampieAppServer.Models;

namespace StampieAppServer.Areas.WebApi.Controllers
{
    public class CommentController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("comment/getAllComments")]
        public IEnumerable<Comment> GetAllComments()
        {
            return db.Comments.Include(c => c.Creator);
        }

        [HttpGet]
        [Authorize]
        [System.Web.Mvc.Route("comment/getUserComments")]
        public IEnumerable<Comment> GetUserComments(Guid userId)
        {
            return db.Comments.Include(c => c.Creator).Where(c => c.Creator.Id == userId);
        }

        [HttpPost]
        [Authorize]
        [System.Web.Mvc.Route("comment/addComment")]
        public Comment AddComment([FromBody] Comment comment)
        {
            comment.Creator = db.AppUsers.FirstOrDefault(u => u.Id == comment.Creator.Id);
            comment.PositiveRate = 0;
            comment.NegativeRate = 0;

            comment = db.Comments.Add(comment);
            db.SaveChanges();

            return comment;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("comment/changeComment")]
        public Comment ChangeComment([FromBody] Comment comment)
        {
            Comment existingComment = db.Comments.Find(comment.Id);
            if (existingComment != null)
            {
                db.Comments.AddOrUpdate(db.Comments.Find(comment.Id), comment);
                db.SaveChanges();
            }
            return comment;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("comment/editCommentText")]
        public Comment EditCommentText(Guid userId, Guid commentId, [FromBody] string text)
        {
            Comment comment = db.Comments.FirstOrDefault(c => c.Id == commentId);
            if (comment != null)
            {
                // only the creator can update a comment text
                User user = db.AppUsers.Find(userId);
                if (user != null)
                {
                    if ((user != null) && (user.Id.Equals(comment.Creator.Id)))
                    {
                        comment.Text = text;
                        db.SaveChanges();
                    }
                    else
                    {
                        comment = null;
                    }
                }
                else
                {
                    comment = null;
                }
            }

            return comment;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("comment/incPositiveRate")]
        public Comment IncPositiveRate(Guid id)
        {
            Comment comment = db.Comments.FirstOrDefault(c => c.Id == id);
            if (comment != null)
            {
                comment.PositiveRate++;
                db.SaveChanges();
            }

            return comment;
        }

        [HttpPut]
        [Authorize]
        [System.Web.Mvc.Route("comment/incNegativeRate")]
        public Comment IncNegativeRate(Guid id)
        {
            Comment comment = db.Comments.FirstOrDefault(c => c.Id == id);
            if (comment != null)
            {
                comment.NegativeRate++;
                db.SaveChanges();
            }

            return comment;
        }
    }
}
