using System;
using System.Collections.Generic;
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
    public class CommentController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [Route("comments/getAllComments")]
        public IEnumerable<Comment> GetAllComments()
        {
            return db.Comments;
        }

        [Route("comments/getUserComments")]
        public IEnumerable<Comment> GetUserComments(User user)
        {
            return db.Comments.Where(c => c.Creator == user);
        }

        [Route("comments/addComment")]
        public Comment AddComment(AddCommentModel model)
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
    }
}
