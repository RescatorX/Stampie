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
        public Guid Id { get; set; }

        [Required]
        public CommentType Type { get; set; }

        [Required]
        [ForeignKey("CreatorId")]
        public User Creator { get; set; }

        [ForeignKey("ParentId")]
        public Comment Parent { get; set; }

        [ForeignKey("StampId")]
        public Stamp Stamp { get; set; }

        [ForeignKey("PhotoId")]
        public Photo Photo { get; set; }

        [ForeignKey("GameId")]
        public Game Game { get; set; }

        [ForeignKey("StatisticId")]
        public Statistic Statistic { get; set; }

        [Required]
        public string Text { get; set; }

        [Required]
        public uint PositiveRate { get; set; }

        [Required]
        public uint NegativeRate { get; set; }
    }
}