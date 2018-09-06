using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class UserGame : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        [ForeignKey("Id")]
        public User User { get; set; }

        [Required]
        [ForeignKey("Id")]
        public Game Game { get; set; }

        [Required]
        public double Score { get; set; }

        [Required]
        public DateTime Played { get; set; }
    }
}