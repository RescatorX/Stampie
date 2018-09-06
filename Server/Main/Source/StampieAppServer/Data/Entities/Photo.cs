using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public class Photo : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [ForeignKey("Id")]
        public User Creator { get; set; }

        public string Description { get; set; }

        public double GpsPositionLat { get; set; }
        public double GpsPositionLng { get; set; }
    }
}