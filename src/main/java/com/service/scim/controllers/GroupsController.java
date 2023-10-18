/** Copyright © 2018, Okta, Inc.
 *
 *  Licensed under the MIT license, the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     https://opensource.org/licenses/MIT
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.IGroupsService;
import com.service.scim.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Groups
 */
@Controller
@RequestMapping("/scim/v2/Groups")
public class GroupsController {

    IGroupsService groupsService;

    @Autowired
    public GroupsController(IGroupsService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * Support pagination and filtering by displayName
     * @param params Payload from HTTP request
     * @return JSON {@link Map} {@link ListResponse}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map groupsGet(@RequestParam Map<String, String> params) {
        return groupsService.groupsGet(params);
    }

    /**
     * Creates new {@link Group} with given attributes
     * @param params JSON {@link Map} of {@link Group} attributes
     * @param response HTTP response
     * @return JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Map groupsPost(@RequestBody Map<String, Object> params, HttpServletResponse response){
        return groupsService.groupsPost(params, response);
    }
}
