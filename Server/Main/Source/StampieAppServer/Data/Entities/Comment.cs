using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

using StampieAppServer.Data.Codebooks;

namespace StampieAppServer.Data.Entities
{
    public class Comment : IEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [Required]
        public CommentType Type { get; set; }

        [Required]
        public User Creator { get; set; }

        public Comment Parent { get; set; }

        public Guid CommentEntity { get; set; }

        [Required]
        public string Text { get; set; }

        [Required]
        public uint PositiveRate { get; set; }

        [Required]
        public uint NegativeRate { get; set; }
    }
}