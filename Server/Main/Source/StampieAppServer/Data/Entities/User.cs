using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class User : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        [ForeignKey("Id")]
        public Account Account { get; set; }

        public string Firstname { get; set; }
        public string Lastname { get; set; }

        public DateTime Birthday { get; set; }
    }
}