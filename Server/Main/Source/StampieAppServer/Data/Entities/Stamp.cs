using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

using StampieAppServer.Data.Codebooks;

namespace StampieAppServer.Data.Entities
{
    public class Stamp : IEntity
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        public StampType Type { get; set; }

        [Required]
        public string StampId { get; set; }

        [Required]
        public string Name { get; set; }

        [Required]
        public string Category { get; set; }

        [Required]
        public string County { get; set; }

        [Required]
        public DateTime Published { get; set; }

        public string SellingPlace1 { get; set; }
        public string SellingPlace1Web { get; set; }
        public string SellingPlace2 { get; set; }
        public string SellingPlace2Web { get; set; }
        public string SellingPlace3 { get; set; }
        public string SellingPlace3Web { get; set; }
        public string SellingPlace4 { get; set; }
        public string SellingPlace4Web { get; set; }
        public string SellingPlace5 { get; set; }
        public string SellingPlace5Web { get; set; }
        public string SellingPlace6 { get; set; }
        public string SellingPlace6Web { get; set; }
        public string SellingPlace7 { get; set; }
        public string SellingPlace7Web { get; set; }
        public string SellingPlace8 { get; set; }
        public string SellingPlace8Web { get; set; }
        public string SellingPlace9 { get; set; }
        public string SellingPlace9Web { get; set; }
        public string SellingPlace10 { get; set; }
        public string SellingPlace10Web { get; set; }
        public string SellingPlace11 { get; set; }
        public string SellingPlace11Web { get; set; }

        public double GpsPositionLat { get; set; }
        public double GpsPositionLng { get; set; }
    }
}