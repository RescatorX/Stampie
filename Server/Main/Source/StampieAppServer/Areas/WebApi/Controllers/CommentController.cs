using System;
using System.Collections.Generic;
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
        [Route("comments/getAllComments")]
        public IEnumerable<Comment> GetAllComments()
        {
            return db.Comments;
        }

        [HttpGet]
        [Authorize]
        [Route("comments/getUserComments")]
        public IEnumerable<Comment> GetUserComments(User user)
        {
            return db.Comments.Where(c => c.Creator == user);
        }

        [HttpPost]
        [Authorize]
        [Route("comments/addComment")]
        public Comment AddComment([FromBody] AddCommentModel model)
        {
            Comment comment = new Comment();
            comment.Creator = model.Creator;
            comment.Parent = model.Parent;
            comment.Text = model.Text;
            comment.PositiveRate = model.PositiveRate;
            comment.NegativeRate = model.NegativeRate;
            
            switch (model.Type)
            {
                case CommentType.Place: comment.Stamp = (Stamp)model.CommentEntity; break;
                case CommentType.Photo: comment.Photo = (Photo)model.CommentEntity; break;
                case CommentType.Game: comment.Game = (Game)model.CommentEntity; break;
                case CommentType.Statistic: comment.Statistic = (Statistic)model.CommentEntity; break;
            }

            db.Comments.Add(comment);
            db.SaveChanges();

            return comment;
        }

        [HttpPut]
        [Authorize]
        [Route("comments/editComment")]
        public Comment EditComment(Guid id, [FromBody] string text)
        {
            Comment comment = db.Comments.FirstOrDefault(c => c.Id == id);
            if (comment != null)
            {
                User commentCreator = comment.Creator;
                string commentCreatorName = string.Format("{0} {1}", commentCreator.Firstname, commentCreator.Lastname);

                IPrincipal user = HttpContext.Current.User;
                if ((user != null) && (((GenericIdentity)user.Identity).Name.Equals(commentCreatorName)))
                {
                    comment.Text = text;
                    db.SaveChanges();
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
        [Route("comments/incPositiveRate")]
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
        [Route("comments/incNegativeRate")]
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
