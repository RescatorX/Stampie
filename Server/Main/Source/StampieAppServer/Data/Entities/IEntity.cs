using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace StampieAppServer.Data.Entities
{
    public interface IEntity
    {
        /// <summary>
        /// Represents a primary key of an entity.
        /// </summary>
        Guid Id { get; set; }
    }
}