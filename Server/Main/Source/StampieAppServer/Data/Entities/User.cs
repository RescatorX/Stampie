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
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [Required]
        public bool IsAnonymous { get; set; }

        public string Username { get; set; }
        public string Password { get; set; }

        public DateTime LastLogin { get; set; }

        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string Country { get; set; }
        public DateTime Birthday { get; set; }
    }
}