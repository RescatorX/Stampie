using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class Statistic : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        [ForeignKey("Id")]
        public User User { get; set; }
    }
}