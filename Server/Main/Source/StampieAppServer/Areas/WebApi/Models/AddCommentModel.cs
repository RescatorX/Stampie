using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using StampieAppServer.Data.Codebooks;
using StampieAppServer.Data.Entities;

namespace StampieAppServer.Areas.WebApi.Models
{
    public class AddCommentModel
    {
        public CommentType Type { get; set; }
        public User Creator { get; set; }
        public Comment Parent { get; set; }

        public IEntity CommentEntity { get; set; }

        public string Text { get; set; }
        public uint PositiveRate { get; set; }
        public uint NegativeRate { get; set; }
    }
}