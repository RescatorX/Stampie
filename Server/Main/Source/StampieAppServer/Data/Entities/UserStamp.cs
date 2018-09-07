using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class UserStamp : IEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [Required]
        public User User { get; set; }

        [Required]
        public Stamp Stamp { get; set; }

        [Required]
        public DateTime Achieved { get; set; }
    }
}