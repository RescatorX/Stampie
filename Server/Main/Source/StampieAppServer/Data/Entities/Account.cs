using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class Account : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        public bool IsAnonymous { get; set; }

        public string Username { get; set; }
        public string Password { get; set; }
    }
}